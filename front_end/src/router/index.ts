import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router';
import { setupInterceptors } from './interceptors';
import { ROUTES } from './routes';
import GlobalLayout from '@/components/Layout/GlobalLayout.vue';

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
    path: '/',
    component: GlobalLayout,
    children: [
      {
        path: ROUTES.MAIN.path,
        name: ROUTES.MAIN.name,
        component: () => import('@/views/Main.vue'),
        meta: {
          title: ROUTES.MAIN.title,
        },
      },
      {
        path: ROUTES.ATTRACTION.path,
        name: ROUTES.ATTRACTION.name,
        component: () => import('@/views/Attraction.vue'),
        meta: {
          title: ROUTES.ATTRACTION.title,
        },
      },
      {
        path: ROUTES.MY_PLANS.path,
        name: ROUTES.MY_PLANS.name,
        component: () => import('@/views/MyPlan.vue'),
        meta: {
          title: ROUTES.MY_PLANS.title,
        },
      },
      {
        path: ROUTES.MY_PAGE.path,
        name: ROUTES.MY_PAGE.name,
        component: () => import('@/views/MyPage.vue'),
        meta: {
          title: ROUTES.MY_PAGE.title,
        },
        children: [],
      },
      {
        path: ROUTES.CHAT.path,
        name: ROUTES.CHAT.name,
        component: () => import('@/views/Chat.vue'),
        meta: {
          title: ROUTES.CHAT.title,
        },
      },
      {
        path: ROUTES.CHAT_ROOM.path,
        name: ROUTES.CHAT_ROOM.name,
        component: () => import('@/views/Chat.vue'),
        meta: {
          title: ROUTES.CHAT_ROOM.title,
        },
        props: true,
      },
      {
        path: ROUTES.PLAN_MODIFY.path,
        name: ROUTES.PLAN_MODIFY.name,
        component: () => import('@/views/PlanModify.vue'),
        meta: {
          title: ROUTES.PLAN_MODIFY.title,
        },
        children: [
          {
            path: ROUTES.PLAN_MODIFY_ATTRACTION.path,
            name: ROUTES.PLAN_MODIFY_ATTRACTION.name,
            component: () => import('@/views/PlanAttractionSearch.vue'),
            meta: {
              title: ROUTES.PLAN_MODIFY_ATTRACTION.title,
            },
            props: true,
          },
        ],
      },
      {
        path: ROUTES.PLANS.path,
        name: ROUTES.PLANS.name,
        component: () => import('@/views/Plan.vue'),
        meta: {
          title: ROUTES.PLANS.title,
        },
        children: [
          {
            path: ROUTES.PLAN_DETAIL.path,
            name: ROUTES.PLAN_DETAIL.name,
            component: () => import('@/views/PlanDetail.vue'),
            meta: {
              title: ROUTES.PLAN_DETAIL.title,
            },
            props: true,
          },
        ],
      },
      {
        path: ROUTES.STELLA.path,
        name: ROUTES.STELLA.name,
        component: () => import('@/views/ConstellationEditor.vue'),
        meta: {
          title: ROUTES.STELLA.title,
        },
      },
    ],
  },

  {
    path: ROUTES.SHARED.path,
    name: ROUTES.SHARED.name,
    component: () => import('@/views/SharedPlan.vue'),
    meta: {
      title: ROUTES.SHARED.title,
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
