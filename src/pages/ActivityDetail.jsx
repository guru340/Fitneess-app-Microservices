import { useEffect, useState } from 'react'
import { Link as RouterLink, useLocation, useParams } from 'react-router-dom'
import {
  Alert,
  Box,
  Button,
  Card,
  CardContent,
  Chip,
  CircularProgress,
  Container,
  Divider,
  Stack,
  Typography,
} from '@mui/material'
import {
  getActivityDetail,
  getActivityRecommendation,
  getUserRecommendations,
} from '../services/api'

const getRecommendationActivityId = (recommendation) =>
  recommendation?.activityId ?? recommendation?.acitivityId

const findBestRecommendation = (recommendations, activity) => {
  const activityId = activity?.id ?? activity?.Id ?? activity?._id ?? activity?._Id ?? activity?.activityId
  const activityType = activity?.type

  return recommendations.find((item) =>
    activityId && getRecommendationActivityId(item) === activityId
  ) ?? recommendations.find((item) =>
    item.activityType === activityType
  ) ?? [...recommendations].sort((a, b) =>
    new Date(b.createdAt ?? 0) - new Date(a.createdAt ?? 0)
  )[0]
}

const formatType = (type = '') =>
  type.toLowerCase().replace(/^\w/, (letter) => letter.toUpperCase())

const InsightList = ({ title, items }) => (
  <Box className="insight-section">
    <Typography variant="h6">{title}</Typography>
    {items.length > 0 ? (
      <Stack spacing={1.25} sx={{ mt: 1.5 }}>
        {items.map((item, index) => (
          <Box key={index} className="insight-item">
            <span>{index + 1}</span>
            <Typography>{item}</Typography>
          </Box>
        ))}
      </Stack>
    ) : (
      <Typography color="text.secondary" sx={{ mt: 1 }}>
        No guidance available for this section yet.
      </Typography>
    )}
  </Box>
)

const ActivityDetail = () => {
  const { id } = useParams()
  const location = useLocation()
  const hasActivityId = location.state?.hasActivityId ?? !id?.startsWith('local-')
  const [activity, setActivity] = useState(location.state?.activity ?? null)
  const [recommendation, setRecommendation] = useState(null)
  const [recommendationLoading, setRecommendationLoading] = useState(false)
  const [recommendationError, setRecommendationError] = useState('')

  useEffect(() => {
    let ignore = false

    const fetchActivityDetail = async () => {
      if (!id || !hasActivityId) return

      try {
        const activityResponse = await getActivityDetail(id)

        if (!ignore) {
          setActivity(activityResponse.data)
        }
      } catch (error) {
        console.error(error)
      }
    }

    fetchActivityDetail()

    return () => {
      ignore = true
    }
  }, [id, hasActivityId])

  useEffect(() => {
    let ignore = false
    const retryDelays = [0, 1000, 2000, 3000, 5000]

    const fetchRecommendation = async () => {
      const userId = activity?.userId

      if (!hasActivityId && !userId) {
        setRecommendationError('AI recommendation needs an activity ID or user ID from the activity response.')
        return
      }

      setRecommendationLoading(true)
      setRecommendationError('')

      for (const [index, delay] of retryDelays.entries()) {
        if (delay > 0) {
          await wait(delay)
        }

        if (ignore) return

        try {
          const response = hasActivityId
            ? await getActivityRecommendation(id)
            : await getUserRecommendations(userId)
          const recommendationData = Array.isArray(response.data)
            ? findBestRecommendation(response.data, activity)
            : response.data

          if (!ignore && recommendationData) {
            setRecommendation(recommendationData)
            setRecommendationError('')
            setRecommendationLoading(false)
            return
          }
        } catch (error) {
          console.error(error)

          if (!ignore && index === retryDelays.length - 1) {
            setRecommendationError('No AI recommendation found yet. Make sure AI service, Kafka, and Gateway are running.')
          }
        }
      }

      if (!ignore) {
        setRecommendationError('No AI recommendation found for this activity yet.')
        setRecommendationLoading(false)
      }
    }

    if (activity) {
      fetchRecommendation()
    }

    return () => {
      ignore = true
    }
  }, [activity, hasActivityId, id])

  if (!activity) {
    return (
      <Container maxWidth="lg" sx={{ py: 6 }}>
        <Box className="loading-row">
          <CircularProgress size={22} />
          <Typography color="text.secondary">Loading activity...</Typography>
        </Box>
      </Container>
    )
  }

  const calories = activity.caloriesBurned ?? activity.calories
  const activityDate = activity.createdAt
    ? new Date(activity.createdAt).toLocaleString()
    : 'Not available'
  const improvements = recommendation?.improvements ?? []
  const suggestions = recommendation?.suggestion ?? recommendation?.suggestions ?? []
  const safety = recommendation?.safety ?? []

  return (
    <Container maxWidth="lg" sx={{ py: { xs: 3, md: 5 } }}>
      <Button component={RouterLink} to="/activities" variant="text" sx={{ mb: 2 }}>
        Back to dashboard
      </Button>

      <Box className="detail-grid">
        <Card className="activity-detail-card">
          <CardContent>
            <Stack spacing={2.5}>
              <Box>
                <Chip label={formatType(activity.type)} color="primary" />
                <Typography variant="h4" sx={{ mt: 2 }}>
                  Activity details
                </Typography>
                <Typography color="text.secondary">Logged in FITTRACK on {activityDate}</Typography>
              </Box>

              <Box className="detail-stat-grid">
                <Box>
                  <Typography className="detail-stat-value">{activity.duration}</Typography>
                  <Typography className="detail-stat-label">Minutes</Typography>
                </Box>
                <Box>
                  <Typography className="detail-stat-value">{calories}</Typography>
                  <Typography className="detail-stat-label">Calories</Typography>
                </Box>
              </Box>
            </Stack>
          </CardContent>
        </Card>

        <Card className="recommendation-card">
          <CardContent>
            <Box className="section-heading">
              <Box>
                <Typography variant="h5">AI coaching</Typography>
                <Typography color="text.secondary">Personalized recommendations from your FITTRACK AI service.</Typography>
              </Box>
              <Chip label={recommendation ? 'Ready' : 'Pending'} color={recommendation ? 'success' : 'default'} />
            </Box>

            {recommendationLoading && (
              <Box className="loading-row">
                <CircularProgress size={22} />
                <Typography color="text.secondary">Loading AI recommendation...</Typography>
              </Box>
            )}

            {!recommendationLoading && recommendationError && (
              <Alert severity="info">{recommendationError}</Alert>
            )}

            {!recommendationLoading && recommendation && (
              <Stack spacing={3}>
                <Box className="analysis-panel">
                  <Typography variant="h6">Analysis</Typography>
                  <Typography>{recommendation.recommendation}</Typography>
                </Box>

                <Divider />

                <InsightList title="Improvements" items={improvements} />
                <InsightList title="Suggestions" items={suggestions} />
                <InsightList title="Safety Guidelines" items={safety} />
              </Stack>
            )}
          </CardContent>
        </Card>
      </Box>
    </Container>
  )
}

const wait = (delay) => new Promise((resolve) => {
  setTimeout(resolve, delay)
})

export default ActivityDetail
