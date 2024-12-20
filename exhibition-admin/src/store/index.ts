import { createStore } from 'vuex'
import { UserInfo } from '@/types'

interface State {
  user: UserInfo | null;
  isAuthenticated: boolean;
}

interface LoginCredentials {
  username: string;
  password: string;
}

export default createStore<State>({
  state: {
    user: null,
    isAuthenticated: false
  },
  mutations: {
    setUser(state: State, user: UserInfo): void {
      state.user = user
      state.isAuthenticated = true
      localStorage.setItem('isAuthenticated', 'true')
    },
    logout(state: State): void {
      state.user = null
      state.isAuthenticated = false
      localStorage.removeItem('isAuthenticated')
    }
  },
  actions: {
    login({ commit }, credentials: LoginCredentials): Promise<void> {
      // 硬编码的用户名和密码验证
      if (credentials.username === 'admin' && credentials.password === 'Boya@1234') {
        commit('setUser', { username: credentials.username })
        return Promise.resolve()
      }
      return Promise.reject('用户名或密码错误')
    },
    logout({ commit }): void {
      commit('logout')
    }
  }
}) 