export const AUTH = {
  LOGIN: 'v1/user/login',
  LOGOUT: 'v1/user/logout',
  SIGNUP: 'v1/user/signup',
  SIGNOUT: 'v1/user/signout',
  PROFILE: 'v1/user/profile',
  PASSWORD_CHANGE: 'v1/user/password',
  LIKED_ATTRACTIONS: 'v1/user/liked/attractions',
  LIKED_PLANS: 'v1/user/liked/plans',
} as const;

export const PLAN = {
  PLANS: 'v1/plans',
  TAGS: 'v1/plans/tags',
  MY_PLANS: 'v1/user/plans',
  SCHEDULE: (planId: number) => `v1/plans/${planId}/schedule`,
  ATTRACTIONS: (planId: number) => `v1/plans/${planId}/attraction`,
  DETAIL: (planId: number) => `v1/plans/${planId}`,
  LIKE: (planId: number) => `v1/plans/${planId}/like`,
  LOCK_CHECK: (planId: number) => `v1/plans/${planId}/lock-check`,
  LOCK: (planId: number) => `v1/plans/${planId}/lock`,
  UNLOCK: (planId: number) => `v1/plans/${planId}/unlock`,
  INVITE: (planId: number) => `v1/plans/${planId}/invite`,
  LEAVE: (planId: number) => `v1/plans/${planId}/leave`,
} as const;

export const ATTRACTION = {
  ATTRACTIONS: 'v1/attractions',
  DETAIL: (attractionId: number) => `v1/attractions/${attractionId}`,
  LIKE: (attractionId: number) => `v1/attractions/${attractionId}/like`,
  REVIEW: (attractionId: number) => `v1/attractions/${attractionId}/reviews`,
  REVIEW_LIKE: (attractionId: number, reviewId: number) =>
    `v1/attractions/${attractionId}/reviews/${reviewId}/like`,
  SIGUNGU: 'v1/attractions/sigungu',
  FEATURED_TAGS: 'v1/featured/tags',
  FEATURED_ATTRACTIONS: (contentTypeId: number) =>
    `v1/featured/attraction/contentType/${contentTypeId}`,
} as const;

export const STELLA = {
  SHARE: 'v1/stella/create',
  LINK: (link: string) => `v1/stella/${link}`,
} as const;
