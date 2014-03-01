class Bandop.Experiment extends Bandop.Model
  @resourceName: 'experiment'

  @encode 'name'

  @belongsTo 'user', autoload: false

  @hasOne 'algorithm'
  @hasMany 'designs'

class Bandop.ExperimentsController extends Bandop.Controller
  routingKey: 'experiments'

  index: (params) ->
    Bandop.Experiment.load @errorHandler (experiments) =>
      @set('experiments', experiments)

  show: (params) ->
    Bandop.Experiment.find params.id, @errorHandler (experiment) =>
      @set('experiment', experiment)
