import DS from 'ember-data';

export default DS.RESTAdapter.extend({
  host: localStorage.getItem('scriptor:api_host') || window.location.origin,
  headers: {
    'Authorization': 'token ' + localStorage.getItem('scriptor:api_key'),
  }
});
