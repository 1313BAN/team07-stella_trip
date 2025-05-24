export const AUTH = {
  LOGIN: 'v1/user/login',
  LOGOUT: 'v1/user/logout',
  SIGNUP: 'v1/user/signup',
  SIGNOUT: 'v1/user/signout',
} as const;

export const PLAN = {
  PLANS: 'v1/plans',
  TAGS: 'v1/plans/tags',
  MY_PLANS: 'v1/user/plans',
  SCHEDULE: (planId: number) => `v1/plans/${planId}/schedule`,
  ADD_ATTRACTIONS: (planId: number) => `v1/plans/${planId}/attraction`,
  DETAIL: (planId: number) => `v1/plans/${planId}`,
  LIKE: (planId: number) => `v1/plans/${planId}/like`,
  LOCK: (planId: number) => `v1/plans/${planId}/lock-check`,
} as const;

export const ATTRACTION = {
  ATTRACTIONS: 'v1/attractions',
  DETAIL: (attractionId: number) => `v1/attractions/${attractionId}`,
  LIKE: (attractionId: number) => `v1/attractions/${attractionId}/like`,
  REVIEW: (attractionId: number) => `v1/attractions/${attractionId}/reviews`,
  SIGUNGU: 'v1/attractions/sigungu',
} as const;
