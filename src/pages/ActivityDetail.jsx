import { useEffect, useState } from 'react'
import { useLocation, useParams } from 'react-router-dom'
import {
  getActivityDetail,
  getActivityRecommendation,
  getUserRecommendations,
} from '../services/api'
import { Alert, Box, Card, CardContent, CircularProgress, Divider, Typography } from '@mui/material'

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
    return <Typography>Loading...</Typography>
  }

  const calories = activity.caloriesBurned ?? activity.calories
  const activityDate = activity.createdAt
    ? new Date(activity.createdAt).toLocaleString()
    : 'Not available'
  const improvements = recommendation?.improvements ?? []
  const suggestions = recommendation?.suggestion ?? recommendation?.suggestions ?? []
  const safety = recommendation?.safety ?? []

  return (
    <Box sx={{ maxWidth: 800, mx: 'auto', p: 2 }}>
      <Card sx={{ mb: 2 }}>
        <CardContent>
          <Typography variant="h5" gutterBottom>Activity Details</Typography>
          <Typography>Type: {activity.type}</Typography>
          <Typography>Duration: {activity.duration} minutes</Typography>
          <Typography>Calories Burned: {calories}</Typography>
          <Typography>Date: {activityDate}</Typography>
        </CardContent>
      </Card>

      <Card>
        <CardContent>
          <Typography variant="h5" gutterBottom>AI Recommendation</Typography>

          {recommendationLoading && (
            <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
              <CircularProgress size={20} />
              <Typography>Loading AI recommendation...</Typography>
            </Box>
          )}

          {!recommendationLoading && recommendationError && (
            <Alert severity="info">{recommendationError}</Alert>
          )}

          {!recommendationLoading && recommendation && (
            <>
              <Typography variant="h6">Analysis</Typography>
              <Typography paragraph>{recommendation.recommendation}</Typography>

              <Divider sx={{ my: 2 }} />

              <Typography variant="h6">Improvements</Typography>
              {improvements.map((item, index) => (
                <Typography key={index} paragraph>{item}</Typography>
              ))}

              <Divider sx={{ my: 2 }} />

              <Typography variant="h6">Suggestions</Typography>
              {suggestions.map((item, index) => (
                <Typography key={index} paragraph>{item}</Typography>
              ))}

              <Divider sx={{ my: 2 }} />

              <Typography variant="h6">Safety Guidelines</Typography>
              {safety.map((item, index) => (
                <Typography key={index} paragraph>{item}</Typography>
              ))}
            </>
          )}
        </CardContent>
      </Card>
    </Box>
  )
}

const wait = (delay) => new Promise((resolve) => {
  setTimeout(resolve, delay)
})

export default ActivityDetail
