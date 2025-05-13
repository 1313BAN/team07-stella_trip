export interface BackgroundStarsProps {
  starCount?: number;
  maxSize?: number;
  minSize?: number;
}

export interface BackgroundParticlesProps {
  particleCount?: number;
  baseAnimationDuration?: number;
  maxDelay?: number;
  particleClass?: string;
}

export interface BackgroundGradientProps {
  from?: string;
  via?: string;
  to?: string;
  direction?: 'to-br' | 'to-bl' | 'to-tr' | 'to-tl' | 'to-b' | 'to-t' | 'to-l' | 'to-r';
}

export interface BackgroundProps
  extends Omit<BackgroundStarsProps, 'class'>,
    Omit<BackgroundParticlesProps, 'class'>,
    Omit<BackgroundGradientProps, 'class'> {}
