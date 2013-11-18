class Bandop.Design extends Bandop.Model
  @resourceName: 'design'

  @encode 'name', 'cssFile', 'jsFile', 'screenshot', 'stats'
  @belongsTo 'user', autoload: false

  @wrapAccessor 'screenshot', (core) ->
    get: -> core.get.apply(this, arguments) || '/assets/img/placeholder.gif'

class Bandop.DesignsController extends Bandop.Controller
  routingKey: 'designs'

  @beforeAction ->
    if !Bandop.get('currentUser')
      Batman.redirect
        controller: 'users'
        action: 'login'
        redirectController: @routingKey
        redirectAction: @action
        redirectId: @params.id

  index: (params) ->
    Bandop.Design.load (err, designs) =>
      return Bandop.alert('Error Loading Designs') if (err)

      designs.sort (a, b) ->
        return 1 if a.get('stats.weight') < b.get('stats.weight')
        return -1 if a.get('stats.weight') > b.get('stats.weight')
        return 0

      designs[0]?.set('primary', true)
      @set('designs', designs)

  show: (params) ->
    Bandop.Design.find params.id, (err, design) =>
      return Bandop.alert('Error Loading Design') if (err)
      @set('design', design)
      @render()

    @render(false)

  new: (params) ->
    @set('design', new Bandop.Design())

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
              <td>Weight</td>
              <td>#{@get('design.stats.weight') * 100}%</td>
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

    @set('jsEditor', @startCodemirror($('#codemirrorJS')[0], 'javascript', 'jsFile'))
    @set('cssEditor', @startCodemirror($('#codemirrorCSS')[0], 'css', 'cssFile'))

  startCodemirror: (div, mode, keypath) ->
    editor = CodeMirror div,
      mode: mode
      value: @controller.get("design.#{keypath}") || ""
      lineNumbers: true

    editor.on 'change', (doc) =>
      @controller.set("design.#{keypath}", doc.getValue())

    return editor

  die: ->
    @get('jsEditor').off('change')
    @get('cssEditor').off('change')
    super

class Bandop.DesignsShowView extends Bandop.DesignsEditView
class Bandop.DesignsNewView extends Bandop.DesignsEditView
