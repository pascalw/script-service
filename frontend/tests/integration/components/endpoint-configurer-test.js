import { moduleForComponent, test } from 'ember-qunit';
import hbs from 'htmlbars-inline-precompile';

moduleForComponent('endpoint-configurer', 'Integration | Component | endpoint configurer', {
  integration: true
});

test('it renders', function(assert) {
  assert.expect(2);

  // Set any properties with this.set('myProperty', 'value');
  // Handle any actions with this.on('myAction', function(val) { ... });

  this.render(hbs`{{endpoint-configurer}}`);

  assert.equal(this.$().text().trim(), '');

  // Template block usage:
  this.render(hbs`
    {{#endpoint-configurer}}
      template block text
    {{/endpoint-configurer}}
  `);

  assert.equal(this.$().text().trim(), 'template block text');
});
