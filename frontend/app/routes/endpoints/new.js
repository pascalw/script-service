import ApplicationRoute from '../application';

export default ApplicationRoute.extend({
  model() {
    return this.store.createRecord('endpoint');
  },
  actions: {
    save(config) {
      config.save().then(() => {
        this.transitionTo('endpoints.show', config);
      });
    }
  }
});
