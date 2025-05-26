import type { Router } from 'vue-router';
import { useAuthStore } from '@/stores/auth';
import { ROUTES } from './routes';

/**
 * 제목 설정
 * @param router router
 */
const setupTitleInterceptor = (router: Router) => {
  router.beforeEach((to, _, next) => {
    if (to.meta.title) {
      document.title = to.meta.title;
    }
    next();
  });
};

/**
 * 인증 상태 검사
 * @param router router
 */
const setupAuthInterceptor = (router: Router) => {
  router.beforeEach((to, _, next) => {
    if (to.meta.requiresAuth) {
      const authStore = useAuthStore();

      if (!authStore.getIsAuthenticated) {
        next({
          name: ROUTES.HOME.name,
          query: { redirect: to.fullPath },
        });
        return;
      }
    }
    next();
  });
};

/**
 * 페이지 전환 애니메이션 처리
 * @param router router
 */
// const setupTransitionInterceptor = (router: Router) => {
//   router.beforeEach((to, from, next) => {
//     next();
//   });
// };

export const setupInterceptors = (router: Router) => {
  setupTitleInterceptor(router);
  setupAuthInterceptor(router);
  // setupTransitionInterceptor(router);
};
