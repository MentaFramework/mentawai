include_class 'org.mentawai.core.BaseAction'
include_class 'org.mentawai.action.BaseLoginAction'

module Mentawai
  module JRuby
    
    class RubyAction < BaseAction
      
      class << self
        
        def authentication_free(*args)
          
          raise "Cannot be called with 0 arguments!" if args.empty?
           
          self.class_eval do
            define_method(:bypassAuthentication) { |inner_action|
              args.member?(inner_action == nil ? nil : inner_action.intern)
            }
          end
          
        end
        
        def redirect_after_login(*args)
          
          raise "Cannot be called with 0 arguments!" if args.empty?
           
          self.class_eval do
            define_method(:shouldRedirect) { |inner_action|
              args.member?(inner_action == nil ? nil : inner_action.intern)
            }
          end
          
        end
        
      end
      
      def required_fields(val, msg, *args)
        val.requiredFields(msg, args.to_java(:String))   
      end
      
      def set_user_session(user)
        BaseLoginAction.setUserSession(user, session)
      end
      
      def setInput(input)
        def input.[](key)
          getValue(key)
        end
        
        def input.get_object(c)
          
          o = c.new
          
          c.public_instance_methods.grep(/\=$/).each do |m|
            prop = m.sub(/\=$/, '')
            if hasValue(prop)
              o.send(m, getValue(prop))
            end
          end
          
          o
          
        end
        
        super
      end
      
    end
    
  end
end