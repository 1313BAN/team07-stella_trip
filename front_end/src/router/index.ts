import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router';
import { setupInterceptors } from './interceptors';
import { ROUTES } from './routes';

const routes: Array<RouteRecordRaw> = [
  {
    path: ROUTES.HOME.path,
    name: ROUTES.HOME.name,
    component: () => import('@/views/Home.vue'),
    meta: {
      title: ROUTES.HOME.title,
    },
  },
  {
    path: ROUTES.NOT_FOUND.path,
    name: ROUTES.NOT_FOUND.name,
    component: () => import('@/views/NotFound.vue'),
    meta: {
      title: ROUTES.NOT_FOUND.title,
    },
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

setupInterceptors(router);

export default router;
