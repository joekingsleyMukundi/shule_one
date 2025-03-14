(function() { $(function() {
      
for(var i=0;i<=5;i++){	
     
	 var brick = "<div class='brick small'>p</div>";
 	  $('.gridly').append($(brick).width(25).height(25));
		
}
	
    return $('.gridly').gridly();
  });

}).call(this);

 
(function() {
  "use strict";
  var $, Animation, Draggable, Gridly,
    bind = function(fn, me){ return function(){ return fn.apply(me, arguments); }; },
    slice = [].slice;

  $ = jQuery;

  Animation = (function() {
    function Animation() {}

    Animation.transitions = {
      "webkitTransition": "webkitTransitionEnd",
      "mozTransition": "mozTransitionEnd",
      "oTransition": "oTransitionEnd",
      "transition": "transitionend"
    };

    Animation.transition = function($el) {
      var el, ref, result, type;
      el = $el[0];
      ref = this.transitions;
      for (type in ref) {
        result = ref[type];
        if (el.style[type] != null) {
          return result;
        }
      }
    };

    Animation.execute = function($el, callback) {
      var transition;
      transition = this.transition($el);
      if (transition != null) {
        return $el.one(transition, callback);
      } else {
        return callback();
      }
    };

    return Animation;

  })();

  Draggable = (function() {
    function Draggable($container, selector, callbacks) {
      this.touchend = bind(this.touchend, this);
      this.click = bind(this.click, this);
      this.moved = bind(this.moved, this);
      this.ended = bind(this.ended, this);
      this.began = bind(this.began, this);
      this.coordinate = bind(this.coordinate, this);
      this.off = bind(this.off, this);
      this.on = bind(this.on, this);
      this.toggle = bind(this.toggle, this);
      this.bind = bind(this.bind, this);
      this.$container = $container;
      this.selector = selector;
      this.callbacks = callbacks;
      this.toggle();
    }

    Draggable.prototype.bind = function(method) {
      if (method == null) {
        method = 'on';
      }
      $(document)[method]('mousemove touchmove', this.moved);
      return $(document)[method]('mouseup touchcancel', this.ended);
    };

    Draggable.prototype.toggle = function(method) {
      if (method == null) {
        method = 'on';
      }
      this.$container[method]('mousedown touchstart', this.selector, this.began);
      this.$container[method]('touchend', this.selector, this.touchend);
      return this.$container[method]('click', this.selector, this.click);
    };

    Draggable.prototype.on = function() {
      return this.toggle('on');
    };

    Draggable.prototype.off = function() {
      return this.toggle('off');
    };
	
	

    Draggable.prototype.coordinate = function(event) {
      switch (event.type) {
        case 'touchstart':
        case 'touchmove':
        case 'touchend':
        case 'touchcancel':
          return event.originalEvent.touches[0];
        default:
          return event;
      }
    };

    Draggable.prototype.began = function(event) {
      var ref;
      if (this.$target) {
        return;
      }
      event.preventDefault();
      event.stopPropagation();
      this.bind('on');
      this.$target = $(event.target).closest(this.$container.find(this.selector));
      this.$target.addClass('dragging');
      this.origin = {
        x: this.coordinate(event).pageX - this.$target.position().left,
        y: this.coordinate(event).pageY - this.$target.position().top
      };
      return (ref = this.callbacks) != null ? typeof ref.began === "function" ? ref.began(event) : void 0 : void 0;
    };

    Draggable.prototype.ended = function(event) {
      var ref;
      if (this.$target == null) {
        return;
      }
      if (event.type !== 'touchend') {
        event.preventDefault();
        event.stopPropagation();
      }
      this.bind('off');
      this.$target.removeClass('dragging');
      delete this.$target;
      delete this.origin;
      return (ref = this.callbacks) != null ? typeof ref.ended === "function" ? ref.ended(event) : void 0 : void 0;
    };

    Draggable.prototype.moved = function(event) {
      var ref;
      if (this.$target == null) {
        return;
      }
      event.preventDefault();
      event.stopPropagation();
      this.$target.css({
        left: this.coordinate(event).pageX - this.origin.x,
        top: this.coordinate(event).pageY - this.origin.y
      });
      this.dragged = this.$target;
      return (ref = this.callbacks) != null ? typeof ref.moved === "function" ? ref.moved(event) : void 0 : void 0;
    };

    Draggable.prototype.click = function(event) {
      if (!this.dragged) {
        return;
      }
      event.preventDefault();
      event.stopPropagation();
      return delete this.dragged;
    };

    Draggable.prototype.touchend = function(event) {
      this.ended(event);
      return this.click(event);
    };

    return Draggable;

  })();
  
  
  
  

  Gridly = (function() {
   
   Gridly.settings = {
     
      gutter: 20,
      columns: 5000,
    
	draggable: {
        zIndex: 800,
        selector: '> *'
      }
    };

    Gridly.gridly = function($el, options) {
      var data;
      if (options == null) {
        options = {};
      }
      data = $el.data('_gridly');
      if (data) {
        $.extend(data.settings, options);
      } else {
        data = new Gridly($el, options);
        $el.data('_gridly', data);
      }
      return data;
    };

    function Gridly($el, settings) {
      if (settings == null) {
        settings = {};
      }
      this.optimize = bind(this.optimize, this);
      this.layout = bind(this.layout, this);
      this.structure = bind(this.structure, this);
      this.position = bind(this.position, this);
      this.size = bind(this.size, this);
      this.draggingMoved = bind(this.draggingMoved, this);
      this.draggingEnded = bind(this.draggingEnded, this);
      this.draggingBegan = bind(this.draggingBegan, this);
      this.$sorted = bind(this.$sorted, this);
      this.draggable = bind(this.draggable, this);
      this.compare = bind(this.compare, this);
      this.$ = bind(this.$, this);
      this.$el = $el;
      this.settings = $.extend({}, Gridly.settings, settings);
      if (this.settings.draggable !== false) {
        this.draggable();
      }
      return this;
    }

    

    Gridly.prototype.$ = function(selector) {
      return this.$el.find(selector);
    };

    Gridly.prototype.compare = function(d, s) {
      if (d.y > s.y + s.h) {
        return +1;
      }
      if (s.y > d.y + d.h) {
        return -1;
      }
      if ((d.x + (d.w / 2)) > (s.x + (s.w / 2))) {
        return +1;
      }
      if ((s.x + (s.w / 2)) > (d.x + (d.w / 2))) {
        return -1;
      }
      return 0;
    };
	
	

    Gridly.prototype.draggable = function(method) {
      if (this._draggable == null) {
        this._draggable = new Draggable(this.$el, this.settings.draggable.selector, {
          began: this.draggingBegan,
          ended: this.draggingEnded,
          moved: this.draggingMoved
        });
      }
      if (method != null) {
        return this._draggable[method]();
      }
    };
	
	

    Gridly.prototype.$sorted = function($elements) {
      return ($elements || this.$('> *'))
    };

    Gridly.prototype.draggingBegan = function(event) {
      var $elements, ref, ref1;
      $elements = this.$sorted();
      setTimeout(this.layout, 0);
      return (ref = this.settings) != null ? (ref1 = ref.callbacks) != null ? typeof ref1.reordering === "function" ? ref1.reordering($elements) : void 0 : void 0 : void 0;
    };

    Gridly.prototype.draggingEnded = function(event) {
      var $elements, ref, ref1;
      $elements = this.$sorted();
       setTimeout(this.layout, 0);
      return (ref = this.settings) != null ? (ref1 = ref.callbacks) != null ? typeof ref1.reordered === "function" ? ref1.reordered($elements, this._draggable.dragged) : void 0 : void 0 : void 0;
    };

    Gridly.prototype.draggingMoved = function(event) {
      var $dragging, $elements, element, i, index, j, k, len, original, positions, ref, ref1, ref2;
      $dragging = $(event.target).closest(this.$(this.settings.draggable.selector));
      $elements = this.$sorted(this.$(this.settings.draggable.selector));
      positions = this.structure($elements).positions;
      original = index = $dragging.data('position');
      ref = positions.filter(function(position) {
        return position.$element.is($dragging);
      });
	  
      for (j = 0, len = ref.length; j < len; j++) {
        element = ref[j];
        element.x = $dragging.position().left;
        element.y = $dragging.position().top;
        element.w = $dragging.data('width') || $dragging.outerWidth();
        element.h = $dragging.data('height') || $dragging.outerHeight();
      }
	  
     $elements = positions.map(function(position) {
        return position.$element;
      });
	  
	  
	  
      $elements = (((ref1 = this.settings.callbacks) != null ? ref1.optimize : void 0) || this.optimize)($elements);
      
      return this.layout();
    };

    Gridly.prototype.size = function($element) {
      return (($element.data('width') || $element.outerWidth()) + this.settings.gutter) / (this.settings.gutter);
    };

    Gridly.prototype.position = function($element, columns,x,y) {
      
      return {
        x: x,
        y: y
      };
    };

    Gridly.prototype.structure = function($elements) {
      var $element, columns, i, index, j, position, positions, ref;
      
      positions = [];
      columns = (function() {
        var j, ref, results1;
        results1 = [];
        for (i = j = 0, ref = this.settings.columns; 0 <= ref ? j <= ref : j >= ref; i = 0 <= ref ? ++j : --j) {
          results1.push(0);
        }
        return results1;
      }).call(this);
      
	  for (index = j = 0, ref = $elements.length; 0 <= ref ? j < ref : j > ref; index = 0 <= ref ? ++j : --j) {
        $element = $($elements[index]);        
		
		position = this.position($element, columns,x,y);
		
		
		
      }
      return {
        height: Math.max.apply(Math, columns),
        positions: positions
      };
    };

    Gridly.prototype.layout = function() {
      var $element, $elements, index, j, position, ref, ref1, structure;
      $elements = (((ref = this.settings.callbacks) != null ? ref.optimize : void 0) || this.optimize)(this.$sorted());
      structure = this.structure($elements);
      for (index = j = 0, ref1 = $elements.length; 0 <= ref1 ? j < ref1 : j > ref1; index = 0 <= ref1 ? ++j : --j) {
        $element = $($elements[index]);
        position = structure.positions[index];
        if ($element.is('.dragging')) {
          continue;
        }
        $element.css({
          position: 'absolute',
          left: position.x,
          top: position.y
        });
      }
      return this.$el.css({
        height: structure.height
      });
    };

    Gridly.prototype.optimize = function(originals) {
      var columns, index, j, ref, results;
      results = [];
      columns = 0;
      while (originals.length > 0) {
        if (columns === this.settings.columns) {
          columns = 0;
        }
        index = 0;
        for (index = j = 0, ref = originals.length; 0 <= ref ? j < ref : j > ref; index = 0 <= ref ? ++j : --j) {
          if (!(columns + this.size($(originals[index])) > this.settings.columns)) {
            break;
          }
        }
        if (index === originals.length) {
          index = 0;
          columns = 0;
        }
        columns += this.size($(originals[index]));
        results.push(originals.splice(index, 1)[0]);
      }
      return results;
    };

    return Gridly;

  })();
  
  
  
  
  
  
  

  $.fn.extend({
    gridly: function() {
      var option, parameters;
      option = arguments[0], parameters = 2 <= arguments.length ? slice.call(arguments, 1) : [];
      if (option == null) {
        option = {};
      }
      return this.each(function() {
        var $this, action, options;
        $this = $(this);
        options = $.extend({}, $.fn.gridly.defaults, typeof option === "object" && option);
        action = typeof option === "string" ? option : option.action;
        if (action == null) {
          action = "layout";
        }
        return Gridly.gridly($this, options)[action](parameters);
      });
    }
  });

}).call(this);
