require 'singleton'
require 'thread'

module Mentawai
  module JRuby
    
    class ActionManager
      include Singleton
      
      def initialize
        @lock = Mutex.new
        @actions = Hash.new
      end
      
      def add_action(id, ruby_class_name)
        action = eval("#{ruby_class_name}.new")        
        @lock.synchronize {
          @actions[id] = action        
        }
        action
      end
      
      def get_action(id)
        action = nil
        @lock.synchronize {
          action = @actions[id]
        }
        action
      end
      
      def del_action(id)
        @lock.synchronize {
          @actions.delete(id)
        }
      end
      
      def size
        s = nil
        @lock.synchronize {
          s = @actions.size
        }
        s
      end
      
    end
    
  end
end