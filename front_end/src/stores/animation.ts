import { defineStore } from 'pinia';

export const useAnimationStore = defineStore('animation', {
  state: () => ({
    hasSeenIntroAnimation: false,
  }),

  actions: {
    setIntroAnimationSeen() {
      this.hasSeenIntroAnimation = true;
    },

    resetIntroAnimation() {
      this.hasSeenIntroAnimation = false;
    },
  },

  persist: {
    key: 'stella-animation-state',
    storage: localStorage,
  },
});
