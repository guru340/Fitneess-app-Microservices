import { Alert, Box, Card, CardActionArea, CardContent, Chip, CircularProgress, Stack, Typography } from '@mui/material'
import { useEffect, useMemo, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { getActivities } from '../services/api'

const getActivityId = (activity) =>
  activity?.id ?? activity?.Id ?? activity?._id ?? activity?._Id ?? activity?.activityId

const formatActivityType = (type = '') =>
  type.toLowerCase().replace(/^\w/, (letter) => letter.toUpperCase())

const ActivityList = ({ refreshKey = 0 }) => {
  const [activities, setActivities] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const navigate = useNavigate()

  useEffect(() => {
    let ignore = false

    const fetchActivities = async () => {
      setLoading(true)
      setError('')

      try {
        const response = await getActivities()

        if (!ignore) {
          setActivities(Array.isArray(response.data) ? response.data : [])
        }
      } catch (requestError) {
        console.error(requestError)

        if (!ignore) {
          setError('Unable to load activities. Check that the gateway and activity service are running.')
        }
      } finally {
        if (!ignore) {
          setLoading(false)
        }
      }
    }

    fetchActivities()

    return () => {
      ignore = true
    }
  }, [refreshKey])

  const summary = useMemo(() => {
    const minutes = activities.reduce((total, activity) => total + Number(activity.duration || 0), 0)
    const calories = activities.reduce((total, activity) => (
      total + Number(activity.caloriesBurned ?? activity.calories ?? 0)
    ), 0)

    return { minutes, calories }
  }, [activities])

  return (
    <Box className="activity-list-panel">
      <Box className="section-heading">
        <Box>
          <Typography variant="h5">Recent activity</Typography>
          <Typography color="text.secondary">Review logged workouts and open any session for AI guidance.</Typography>
        </Box>
        <Chip label={`${activities.length} sessions`} color="primary" variant="outlined" />
      </Box>

      <Box className="summary-strip">
        <Box>
          <Typography className="summary-value">{summary.minutes}</Typography>
          <Typography className="summary-label">Total minutes</Typography>
        </Box>
        <Box>
          <Typography className="summary-value">{summary.calories}</Typography>
          <Typography className="summary-label">Calories burned</Typography>
        </Box>
      </Box>

      {loading && (
        <Box className="loading-row">
          <CircularProgress size={22} />
          <Typography color="text.secondary">Loading activities...</Typography>
        </Box>
      )}

      {!loading && error && <Alert severity="info">{error}</Alert>}

      {!loading && !error && activities.length === 0 && (
        <Card className="empty-state">
          <CardContent>
            <Typography variant="h6">No workouts yet</Typography>
            <Typography color="text.secondary">Add your first activity to start building your FITTRACK timeline.</Typography>
          </CardContent>
        </Card>
      )}

      {!loading && !error && activities.length > 0 && (
        <Box className="activity-card-grid">
          {activities.map((activity, index) => {
            const activityId = getActivityId(activity)
            const hasActivityId = activityId !== undefined && activityId !== null && activityId !== ''
            const calories = activity.caloriesBurned ?? activity.calories
            const routeId = hasActivityId ? activityId : `local-${index}`

            return (
              <Card key={`${routeId}-${index}`} className="activity-card">
                <CardActionArea
                  onClick={() => {
                    navigate(`/activities/${routeId}`, {
                      state: {
                        activity,
                        hasActivityId,
                      },
                    })
                  }}
                >
                  <CardContent>
                    <Stack spacing={2}>
                      <Box className="activity-card-top">
                        <Chip label={formatActivityType(activity.type)} size="small" className="activity-type-chip" />
                        <Typography className="activity-card-index">#{index + 1}</Typography>
                      </Box>
                      <Box>
                        <Typography className="activity-card-value">{activity.duration} min</Typography>
                        <Typography color="text.secondary">{calories} calories burned</Typography>
                      </Box>
                    </Stack>
                  </CardContent>
                </CardActionArea>
              </Card>
            )
          })}
        </Box>
      )}
    </Box>
  )
}

export default ActivityList
