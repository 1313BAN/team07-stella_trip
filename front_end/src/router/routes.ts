export const ROUTES = {
  HOME: { path: '/', name: 'home', title: '메인페이지' },
  MAIN: { path: '/main', name: 'main', title: '메인페이지' },
  NOT_FOUND: { path: '/:pathMatch(.*)*', name: 'not-found', title: '존재하지 않는 페이지' },
} as const;
