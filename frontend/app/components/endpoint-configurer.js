import Ember from 'ember';

export default Ember.Component.extend({
  didInsertElement: function() {
    Ember.run.scheduleOnce('afterRender', this, function() {
      var editorEl = Ember.$(this.element).find('.editor');

      var editor = window.ace.edit(editorEl[0]);
      editor.setTheme('ace/theme/tomorrow_night');
      editor.getSession().setMode('ace/mode/javascript');

      editor.setValue(this.model.get('script') || '', 1);
      editor.scrollToLine(0);

      editor.on('change', () => {
        this.model.set('script', editor.getValue());
      });
    });
  }.observes('model'),

  actions: {
    saveOrCreate(config) {
      // HACK. Id's dynamically set don't seem to included in serialization by default.
      // According to https://github.com/emberjs/data/issues/3694 this is a bug, though I may understand
      // that incorrectly since upgrading to ember-data 2.1.0-beta3 which include this fix doesn't fix my problem :(
      config._internalModel.id = config.id;

      this.sendAction('save', config);
    },
    delete(config) {
      if(window.confirm('Are you sure you want to delete this item?')) {
        this.sendAction('delete', config);
      }
    }
  }
});
