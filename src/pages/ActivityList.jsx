import { Card, CardContent, Grid, Typography } from '@mui/material'

import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom';
import { getActivities } from '../services/api';

const ActivityList = () => {
  const [activities, setActivities] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    let ignore = false;

    const fetchActivities = async () => {
      try {
        const response = await getActivities();

        if (!ignore) {
          setActivities(response.data);
        }
      } catch (error) {
        console.error(error);
      }
    };

    fetchActivities();

    return () => {
      ignore = true;
    };
  }, []);

  const getActivityId = (activity) =>
    activity?.id ?? activity?.Id ?? activity?._id ?? activity?._Id ?? activity?.activityId;

  return (
    <Grid container spacing={2}>
      {activities.map((activity, index) => {
        const activityId = getActivityId(activity);
        const hasActivityId = activityId !== undefined && activityId !== null && activityId !== '';
        const calories = activity.caloriesBurned ?? activity.calories;
        const routeId = hasActivityId ? activityId : `local-${index}`;

        return (
          <Grid
            key={`${routeId}-${index}`}
            container
            spacing={{ xs: 2, md: 3 }}
            columns={{ xs: 4, sm: 8, md: 12 }}
          >
            <Card
              sx={{
                cursor: 'pointer',
              }}
              onClick={() => {
                navigate(`/activities/${routeId}`, {
                  state: {
                    activity,
                    hasActivityId,
                  },
                });
              }}
            >
              <CardContent>
                <Typography variant='h6'>{activity.type}</Typography>
                <Typography>Duration: {activity.duration}</Typography>
                <Typography>Calories: {calories}</Typography>
              </CardContent>
            </Card>
          </Grid>
        );
      })}
    </Grid>
  )
}

export default ActivityList
