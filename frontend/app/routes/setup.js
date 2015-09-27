import Ember from 'ember';
import Setup from '../services/setup';

export default Ember.Route.extend({

  model() {
    return Ember.Object.create({
      defaultHost: window.location.origin,
      host: Setup.getScriptorHost(),
      token: Setup.getScriptorToken()
    });
  },

  actions: {
    setup(config) {
      Setup.setScriptorToken(config.get('token'));

      if(config.get('host')) {
        Setup.setScriptorHost(config.get('host'));
      }

      this.transitionTo('/');
    }
  }
});
