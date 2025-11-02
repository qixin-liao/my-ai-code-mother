import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'Home',
      component: () => import('@/pages/HomePage.vue'),
    },
    {
      path: '/user/login',
      name: 'userLogin',
      component: () => import('@/pages/user/UserLoginPage.vue'),
    },
    {
      path: '/user/register',
      name: 'userRegister',
      component: () => import('@/pages/user/UserRegisterPage.vue'),
    },
    {
      path: '/admin/userManage',
      name: 'userManage',
      component: () => import('@/pages/admin/UserManagePage.vue'),
    },
    {
      path: '/admin/appManage',
      name: 'appManage',
      component: () => import('@/pages/admin/AppManagePage.vue'),
    },
    {
      path: '/app/chat/:id',
      name: 'appChat',
      component: () => import('@/pages/app/AppChatPage.vue'),
    },
    {
      path: '/app/edit/:id',
      name: 'appEdit',
      component: () => import('@/pages/app/AppEditPage.vue'),
    },
    {
      path: '/about',
      name: 'About',
      component: () => import('@/pages/AboutPage.vue'),
    },
    // 404 页面
    {
      path: '/:pathMatch(.*)*',
      name: 'NotFound',
      redirect: '/',
    },
  ],
})

export default router
