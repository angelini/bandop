class Bandop.Design extends Bandop.Model
  @resourceName: 'design'

  @encode 'name', 'cssFile', 'jsFile', 'screenshot'
  @belongsTo 'user', autoload: false

class Bandop.DesignsController extends Batman.Controller
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
    @get('design').save (err, design) ->
      return Bandop.alert('Error Saving Design') if (err)
      Bandop.alert('Design Saved')

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
