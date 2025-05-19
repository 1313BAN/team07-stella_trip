export const AUTH = {
  LOGIN: 'v1/user/login',
  LOGOUT: 'v1/user/logout',
  SIGNUP: 'v1/user/signup',
  SIGNOUT: 'v1/user/signout',
} as const;

export const PLAN = {
  PLANS: 'v1/plans',
  TAGS: 'v1/plans/tags',
  DETAIL: (planId: number) => `v1/plans/${planId}`,
  LIKE: (planId: number) => `v1/plans/${planId}/like`,
} as const;

export const ATTRACTION = {
  ATTRACTIONS: 'v1/attractions',
  DETAIL: (attractionId: number) => `v1/attractions/${attractionId}`,
  LIKE: (attractionId: number) => `v1/attractions/${attractionId}/like`,
} as const;
