class Bandop extends Batman.App
  @root 'designs#index'

  @resources 'designs'

  @route 'login', 'users#login'
  @route 'logout', 'users#logout'

  @alert: (message) ->
    @set('alertMessage', message)
    $('html').animate(scrollTop: 0)

  @dissmissAlert: ->
    @unset('alertMessage')

class Bandop.RestStorage extends Batman.RestStorage
  serializeAsForm: false
  recordJsonNamespace: -> false

class Bandop.Model extends Batman.Model
  @urlPrefix: '/api'
  urlPrefix: '/api'

  @persist Bandop.RestStorage

Bandop.apiRequest = (options) ->
  new Batman.Request
    method: options.method || 'GET'
    contentType: 'application/json'
    url: '/api/' + options.url
    data: JSON.stringify(options.data)
    success: options.success
    error: options.error

$.cookie.raw = true

Batman.View::cache = false
Batman.config.usePushState = false
Batman.config.pathToHTML = '/assets/html'

window.Bandop = Bandop
