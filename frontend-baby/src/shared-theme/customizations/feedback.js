import { alpha } from '@mui/material/styles';
import { gray, chartPastel } from '../themePrimitives';

/* eslint-disable import/prefer-default-export */
export const feedbackCustomizations = {
  MuiAlert: {
    styleOverrides: {
      root: ({ theme }) => ({
        borderRadius: 10,
        backgroundColor: chartPastel.babyBlue,
        color: (theme.vars || theme).palette.text.primary,
        border: `1px solid ${alpha(chartPastel.babyBlue, 0.5)}`,
        '& .MuiAlert-icon': {
          color: chartPastel.babyBlue,
        },
        ...theme.applyStyles('dark', {
          backgroundColor: `${alpha(chartPastel.babyBlue, 0.2)}`,
          border: `1px solid ${alpha(chartPastel.babyBlue, 0.3)}`,
        }),
      }),
    },
  },
  MuiDialog: {
    styleOverrides: {
      root: ({ theme }) => ({
        '& .MuiDialog-paper': {
          borderRadius: '10px',
          border: '1px solid',
          borderColor: (theme.vars || theme).palette.divider,
        },
      }),
    },
  },
  MuiLinearProgress: {
    styleOverrides: {
      root: ({ theme }) => ({
        height: 8,
        borderRadius: 8,
        backgroundColor: gray[200],
        ...theme.applyStyles('dark', {
          backgroundColor: gray[800],
        }),
      }),
    },
  },
};
