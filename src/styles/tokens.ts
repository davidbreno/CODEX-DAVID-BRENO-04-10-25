export const colorVariables = {
  primary: '--color-primary',
  secondary: '--color-secondary',
  success: '--color-success',
  warning: '--color-warning',
  danger: '--color-danger',
  info: '--color-info',
  neutral: '--color-neutral',
  surface: '--color-surface',
  surfaceElevated: '--color-surface-elevated',
  content: '--color-content',
  accent: '--color-accent',
  muted: '--color-muted'
} as const;

export const gradients = {
  primary: {
    start: '#00eaff',
    end: '#8a2be2'
  }
};

export const spacing = {
  xs: 0.5,
  sm: 1,
  md: 1.5,
  lg: 2,
  xl: 3,
  '2xl': 4
};

export const fontFamily = {
  sans: ['Inter', 'system-ui', 'sans-serif'],
  display: ['Space Grotesk', 'system-ui', 'sans-serif']
};

export const typography = {
  heading: {
    fontFamily: fontFamily.display,
    fontWeight: '600'
  },
  body: {
    fontFamily: fontFamily.sans,
    fontWeight: '400'
  }
};

export const withOpacity = (variable: string) =>
  ({ opacityValue }: { opacityValue?: string } = {}) => {
    if (opacityValue === undefined) {
      return `rgb(var(${variable}) / 1)`;
    }
    return `rgb(var(${variable}) / ${opacityValue})`;
  };
