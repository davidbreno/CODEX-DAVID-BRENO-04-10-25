import type { Config } from 'tailwindcss';
import { colorVariables, fontFamily, spacing, withOpacity } from './src/styles/tokens';

const config: Config = {
  darkMode: 'class',
  content: ['./index.html', './src/**/*.{ts,tsx,html}'],
  theme: {
    extend: {
      colors: {
        primary: withOpacity(colorVariables.primary),
        secondary: withOpacity(colorVariables.secondary),
        success: withOpacity(colorVariables.success),
        warning: withOpacity(colorVariables.warning),
        danger: withOpacity(colorVariables.danger),
        info: withOpacity(colorVariables.info),
        neutral: withOpacity(colorVariables.neutral),
        surface: withOpacity(colorVariables.surface),
        'surface-elevated': withOpacity(colorVariables.surfaceElevated),
        content: withOpacity(colorVariables.content),
        accent: withOpacity(colorVariables.accent),
        muted: withOpacity(colorVariables.muted)
      },
      backgroundImage: {
        primary: 'linear-gradient(135deg, var(--gradient-start), var(--gradient-end))'
      },
      fontFamily,
      spacing: {
        ...Object.fromEntries(Object.entries(spacing).map(([key, value]) => [key, `${value}rem`]))
      }
    }
  },
  plugins: []
};

export default config;
