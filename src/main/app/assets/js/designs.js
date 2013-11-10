// Generated by CoffeeScript 1.6.3
(function() {
  var _ref, _ref1, _ref2, _ref3, _ref4,
    __hasProp = {}.hasOwnProperty,
    __extends = function(child, parent) { for (var key in parent) { if (__hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; };

  Bandop.Design = (function(_super) {
    __extends(Design, _super);

    function Design() {
      _ref = Design.__super__.constructor.apply(this, arguments);
      return _ref;
    }

    Design.resourceName = 'design';

    Design.encode('name', 'cssFile', 'jsFile', 'screenshot');

    Design.belongsTo('user', {
      autoload: false
    });

    return Design;

  })(Bandop.Model);

  Bandop.DesignsController = (function(_super) {
    __extends(DesignsController, _super);

    function DesignsController() {
      _ref1 = DesignsController.__super__.constructor.apply(this, arguments);
      return _ref1;
    }

    DesignsController.prototype.routingKey = 'designs';

    DesignsController.beforeAction(function() {
      if (!Bandop.get('currentUser')) {
        return Batman.redirect({
          controller: 'users',
          action: 'login',
          redirectController: this.routingKey,
          redirectAction: this.action,
          redirectId: this.params.id
        });
      }
    });

    DesignsController.prototype.index = function(params) {
      var _this = this;
      return Bandop.Design.load(function(err, designs) {
        if (err) {
          return Bandop.alert('Error Loading Designs');
        }
        return _this.set('designs', designs);
      });
    };

    DesignsController.prototype.show = function(params) {
      var _this = this;
      Bandop.Design.find(params.id, function(err, design) {
        if (err) {
          return Bandop.alert('Error Loading Design');
        }
        _this.set('design', design);
        return _this.render();
      });
      return this.render(false);
    };

    DesignsController.prototype["new"] = function(params) {
      return this.set('design', new Bandop.Design());
    };

    DesignsController.prototype.save = function() {
      return this.get('design').save(function(err, design) {
        if (err) {
          return Bandop.alert('Error Saving Design');
        }
        return Bandop.alert('Design Saved');
      });
    };

    return DesignsController;

  })(Batman.Controller);

  Bandop.DesignsEditView = (function(_super) {
    __extends(DesignsEditView, _super);

    function DesignsEditView() {
      _ref2 = DesignsEditView.__super__.constructor.apply(this, arguments);
      return _ref2;
    }

    DesignsEditView.prototype.viewDidAppear = function() {
      if (this.get('jsEditor')) {
        return;
      }
      this.set('jsEditor', this.startCodemirror($('#codemirrorJS')[0], 'javascript', 'jsFile'));
      return this.set('cssEditor', this.startCodemirror($('#codemirrorCSS')[0], 'css', 'cssFile'));
    };

    DesignsEditView.prototype.startCodemirror = function(div, mode, keypath) {
      var editor,
        _this = this;
      editor = CodeMirror(div, {
        mode: mode,
        value: this.controller.get("design." + keypath) || "",
        lineNumbers: true
      });
      editor.on('change', function(doc) {
        return _this.controller.set("design." + keypath, doc.getValue());
      });
      return editor;
    };

    DesignsEditView.prototype.die = function() {
      this.get('jsEditor').off('change');
      this.get('cssEditor').off('change');
      return DesignsEditView.__super__.die.apply(this, arguments);
    };

    return DesignsEditView;

  })(Batman.View);

  Bandop.DesignsShowView = (function(_super) {
    __extends(DesignsShowView, _super);

    function DesignsShowView() {
      _ref3 = DesignsShowView.__super__.constructor.apply(this, arguments);
      return _ref3;
    }

    return DesignsShowView;

  })(Bandop.DesignsEditView);

  Bandop.DesignsNewView = (function(_super) {
    __extends(DesignsNewView, _super);

    function DesignsNewView() {
      _ref4 = DesignsNewView.__super__.constructor.apply(this, arguments);
      return _ref4;
    }

    return DesignsNewView;

  })(Bandop.DesignsEditView);

}).call(this);