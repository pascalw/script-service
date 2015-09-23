import DS from 'ember-data';

export default DS.Model.extend({
  contentType: DS.attr(),
  script: DS.attr(),
  accessToken: DS.attr()
});
