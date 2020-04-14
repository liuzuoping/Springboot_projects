import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/HelloWorld'
import xiaoliu from "../components/xiaoliu"
Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/aaa',
      name: 'HelloWorld',
      component: HelloWorld
    },{
      path: '/hello',
      name: 'xiaoliu',
      component: xiaoliu
    }
  ]
})
