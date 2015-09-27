import Ember from 'ember';
import SetupRequiredRouteMixin from '../../../mixins/setup-required-route';
import { module, test } from 'qunit';

module('Unit | Mixin | setup required route');

// Replace this with your real tests.
test('it works', function(assert) {
  var SetupRequiredRouteObject = Ember.Object.extend(SetupRequiredRouteMixin);
  var subject = SetupRequiredRouteObject.create();
  assert.ok(subject);
});
