import DS from 'ember-data';
import Setup from '../services/setup';

export default DS.RESTAdapter.extend({
  host: Setup.getScriptorHost() || window.location.origin,
  headers: {
    'Authorization': 'token ' + Setup.getScriptorToken(),
  }
});
