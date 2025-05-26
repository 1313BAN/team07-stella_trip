export const ROUTES = {
  HOME: { path: '/', name: 'home', title: '메인페이지' },
  MAIN: { path: '/main', name: 'main', title: '메인페이지' },
  ATTRACTION: { path: '/attractions', name: 'attractions', title: '여행지 검색' },
  PLANS: { path: '/plans', name: 'plans', title: '여행 계획 목록' },
  MY_PLANS: { path: '/my-plans', name: 'my-plans', title: '내 여행 계획 목록' },
  PLAN_MODIFY: { path: '/plan-modify/:planId', name: 'plan-modify', title: '여행 계획 수정' },
  PLAN_MODIFY_ATTRACTION: {
    path: '/plan-modify/:planId/search',
    name: 'plan-modify-search',
    title: '여행 계획 수정',
  },
  PLAN_DETAIL: { path: '/plans/:planId', name: 'plan-detail', title: '여행 계획 상세' },
  SHARED: { path: '/shared/:link', name: 'shared-plan', title: '공유된 여행 계획' },
  NOT_FOUND: { path: '/:pathMatch(.*)*', name: 'not-found', title: '존재하지 않는 페이지' },
  MY_PAGE: { path: '/my-page', name: 'my-page', title: '마이 페이지' },
} as const;
