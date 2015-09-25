import Ember from 'ember';

export default Ember.Route.extend({
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
