import {
  Box,
  Button,
  Card,
  CardContent,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
  Stack,
  TextField,
  Typography,
} from '@mui/material'
import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { addActivity } from '../services/api'

const activityOptions = [
  { value: 'RUNNING', label: 'Running', helper: 'Pace, endurance, and calorie burn' },
  { value: 'WALKING', label: 'Walking', helper: 'Daily movement and steady recovery' },
  { value: 'CYCLING', label: 'Cycling', helper: 'Cardio power and low-impact miles' },
]

const initialActivity = {
  type: 'RUNNING',
  duration: '',
  calories: '',
  additionalMetrics: {},
}

const getActivityId = (activityResponse) =>
  activityResponse?.id ?? activityResponse?.Id ?? activityResponse?._id ?? activityResponse?._Id ?? activityResponse?.activityId

const ActivityForm = ({ onActivityAdded }) => {
  const navigate = useNavigate()
  const [activity, setActivity] = useState(initialActivity)
  const [submitting, setSubmitting] = useState(false)

  const selectedActivity = activityOptions.find((option) => option.value === activity.type)

  const handleSubmit = async (e) => {
    e.preventDefault()
    setSubmitting(true)

    try {
      const response = await addActivity(activity)
      const savedActivity = response.data
      const activityId = getActivityId(savedActivity)

      setActivity(initialActivity)

      if (activityId) {
        navigate(`/activities/${activityId}`, {
          state: {
            activity: savedActivity,
            hasActivityId: true,
          },
        })
      } else {
        onActivityAdded?.()
      }
    } catch (error) {
      console.error(error)
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <Card className="activity-form-card">
      <CardContent>
        <Stack spacing={1} sx={{ mb: 3 }}>
          <Typography variant="h5">Log a workout</Typography>
          <Typography color="text.secondary">
            Capture your latest session and FITTRACK will route you to AI feedback when it is ready.
          </Typography>
        </Stack>

        <Box component="form" onSubmit={handleSubmit}>
          <Stack spacing={2.5}>
            <FormControl fullWidth>
              <InputLabel>Activity Type</InputLabel>
              <Select
                label="Activity Type"
                value={activity.type}
                onChange={(e) => setActivity({ ...activity, type: e.target.value })}
              >
                {activityOptions.map((option) => (
                  <MenuItem key={option.value} value={option.value}>
                    {option.label}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>

            <Box className="activity-note">
              <Typography className="activity-note-title">{selectedActivity?.label}</Typography>
              <Typography className="activity-note-copy">{selectedActivity?.helper}</Typography>
            </Box>

            <TextField
              fullWidth
              required
              label="Duration"
              type="number"
              value={activity.duration}
              onChange={(e) => setActivity({ ...activity, duration: e.target.value })}
              inputProps={{ min: 1 }}
              helperText="Minutes"
            />

            <TextField
              fullWidth
              required
              label="Calories Burned"
              type="number"
              value={activity.calories}
              onChange={(e) => setActivity({ ...activity, calories: e.target.value })}
              inputProps={{ min: 1 }}
              helperText="Estimated active calories"
            />

            <Button type="submit" variant="contained" size="large" disabled={submitting}>
              {submitting ? 'Saving activity...' : 'Add activity'}
            </Button>
          </Stack>
        </Box>
      </CardContent>
    </Card>
  )
}

export default ActivityForm
