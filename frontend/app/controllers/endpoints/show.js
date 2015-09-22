import Ember from 'ember';

export default Ember.Controller.extend({
  actions: {
    delete(config) {
      config.destroyRecord().then(() => {
        this.transitionToRoute('/');
      });
    }
  }
});
