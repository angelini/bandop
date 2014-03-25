class Bandop.Design extends Bandop.Model
  @resourceName: 'design'

  @encode 'name', 'css_file', 'js_file', 'screenshot', 'stats', 'experiment_id'
  @belongsTo 'experiment'

  @wrapAccessor 'screenshot', (core) ->
    get: -> core.get.apply(this, arguments) || '/assets/img/placeholder.gif'

class Bandop.DesignsController extends Bandop.Controller
  routingKey: 'designs'

  show: (params) ->
    Bandop.Design.find params.id, @errorHandler (design) =>
      @set('design', design)
      @render()

    @render(false)

  new: (params) ->
    @set('design', design = new Bandop.Design(experiment_id: params.experimentId))

  save: ->
    @get('design').save (request, design) ->
      return Bandop.alert('Error Saving Design') if (request? && request.status != 201)
      Bandop.alert('Design Saved')

class Bandop.DesignIterationView extends Batman.View
  viewDidAppear: ->
    @get('$node').find('img').popover
      trigger: 'hover'
      html: true
      title: 'Design Stats'
      content: """
        <table class="table table-borderless">
          <tbody>
            <tr>
              <td>Probability</td>
              <td>#{new Number(@get('design.stats.prob') * 100).toFixed(0)}%</td>
            </tr>
            <tr>
              <td>Count</td>
              <td>#{@get('design.stats.count')}</td>
            </tr>
          </tbody>
        </table>
      """

class Bandop.DesignsEditView extends Batman.View
  viewDidAppear: ->
    return if @get('jsEditor')

    @set('jsEditor', @startCodemirror($('#codemirrorJS')[0], 'javascript', 'js_file'))
    @set('cssEditor', @startCodemirror($('#codemirrorCSS')[0], 'css', 'css_file'))

  startCodemirror: (div, mode, keypath) ->
    editor = CodeMirror div,
      mode: mode
      value: @controller.get("design.#{keypath}") || ""
      lineNumbers: true

    editor.on 'change', (doc) =>
      @controller.set("design.#{keypath}", doc.getValue())

    return editor

  save: ->
    @controller.save()
    $('body').scrollTop(0)

  die: ->
    @get('jsEditor').off('change')
    @get('cssEditor').off('change')
    super

class Bandop.DesignsShowView extends Bandop.DesignsEditView
class Bandop.DesignsNewView extends Bandop.DesignsEditView
