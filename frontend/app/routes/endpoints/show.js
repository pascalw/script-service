import Ember from 'ember';

export default Ember.Route.extend({
  model(params) {
    return this.store.findRecord('endpoint', params.id);
  },
  actions: {
    delete(config) {
      config.destroyRecord().then(() => {
        this.transitionTo('/');
      });
    }
  }
});
