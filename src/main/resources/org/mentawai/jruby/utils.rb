module Mentawai
  module JRuby
    
    class Utils
      
      def self.get_prop(action_id, prop_name)
        
        obj = get_action(action_id)
        
        if obj.class.public_method_defined?(prop_name) then
          m = obj.method(prop_name)
          return m.call if m.arity <= 0
        end
        
        nil # nothing was found
      end
      
      private
      
      def self.get_action(id)
        action = Mentawai::JRuby::ActionManager.instance.get_action(id)
        raise "Cannot find action: #{id}" if not action
        action
      end
      
    end
    
  end
end