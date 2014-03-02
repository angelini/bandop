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

  new: (params) ->
    Bandop.AlgorithmType.load @errorHandler (types) =>
      @set('algorithmTypes', types)

      @set('experiment', experiment = new Bandop.Experiment())
      experiment.set('algorithm', algorithm = new Bandop.Algorithm)

      algorithm.observe 'type', (typeId) ->
        type = (types.filter (type) -> "" + type.get('id') == typeId)[0]
        algorithm.set('config', new Batman.Hash(Batman.mixin({}, type.get('defaults'))))

      @render()

    @render(false)

  save: ->
    @get('experiment').save (request, experiment) ->
      return Bandop.alert('Error Saving Experiment') if (request? && request.status != 201)
      Bandop.alert('Experiment Saved')
