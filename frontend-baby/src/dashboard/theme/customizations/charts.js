import { axisClasses, legendClasses, chartsGridClasses } from '@mui/x-charts';

import { gray } from '../../../shared-theme/themePrimitives';

/* eslint-disable import/prefer-default-export */
export const chartsCustomizations = {
  MuiCharts: {
    defaultProps: ({ theme }) => ({
      colors: Object.values((theme.vars || theme).palette.chart),
    }),
  },
  MuiChartsAxis: {
    styleOverrides: {
      root: ({ theme }) => {
        const chartColors = (theme.vars || theme).palette.chart;
        return {
          [`& .${axisClasses.line}`]: {
            stroke: chartColors.babyBlue,
          },
          [`& .${axisClasses.tick}`]: { stroke: chartColors.babyBlue },
          [`& .${axisClasses.tickLabel}`]: {
            fill: gray[500],
            fontWeight: 500,
          },
          ...theme.applyStyles('dark', {
            [`& .${axisClasses.line}`]: {
              stroke: gray[700],
            },
            [`& .${axisClasses.tick}`]: { stroke: gray[700] },
            [`& .${axisClasses.tickLabel}`]: {
              fill: chartColors.babyBlue,
              fontWeight: 500,
            },
          }),
        };
      },
    },
  },
  MuiChartsTooltip: {
    styleOverrides: {
      mark: ({ theme }) => ({
        ry: 6,
        boxShadow: 'none',
        border: `1px solid ${(theme.vars || theme).palette.divider}`,
      }),
      table: ({ theme }) => ({
        border: `1px solid ${(theme.vars || theme).palette.divider}`,
        borderRadius: theme.shape.borderRadius,
        background: 'hsl(0, 0%, 100%)',
        ...theme.applyStyles('dark', {
          background: gray[900],
        }),
      }),
    },
  },
  MuiChartsLegend: {
    styleOverrides: {
      root: {
        [`& .${legendClasses.mark}`]: {
          ry: 6,
        },
      },
    },
  },
  MuiChartsGrid: {
    styleOverrides: {
      root: ({ theme }) => {
        const chartColors = (theme.vars || theme).palette.chart;
        return {
          [`& .${chartsGridClasses.line}`]: {
            stroke: chartColors.mint,
            strokeDasharray: '3 3',
            strokeWidth: 0.5,
          },
          ...theme.applyStyles('dark', {
            [`& .${chartsGridClasses.line}`]: {
              stroke: gray[700],
              strokeDasharray: '3 3',
              strokeWidth: 0.5,
            },
          }),
        };
      },
    },
  },
};
