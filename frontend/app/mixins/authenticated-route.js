import Ember from 'ember';
import Setup from '../services/setup';

export default Ember.Mixin.create({
  beforeModel(transition) {
    var superResult = this._super(transition);

    if(! Setup.isComplete())
      this.transitionTo('setup');

    return superResult;
  }
});
