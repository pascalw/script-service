import ApplicationRoute from '../application';

export default ApplicationRoute.extend({
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
