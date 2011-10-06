module Mentawai
  module JRuby
    class Loader
    
      def initialize(realPath)
        
        @file_mask = realPath + '/WEB-INF/ruby/**/*.rb'
        
        @files = { }
        
        Dir.glob(@file_mask).each do |f|
          next if f !~ /\.rb$/
          @files[File.new(f)] = 0
        end
      end
  
      def showFiles
        @files.each do |f,t|
          puts "#{f.path}: #{t}"
        end
      end
      
      def files_count
        @files.size
      end
      
      alias size files_count
      alias length files_count
      
      def reloadFiles
        count = 0
        @files.each do |f,t|
          if t == 0 then
            puts "Loading #{f.path}" # first time
            load f.path
            count += 1
          elsif t != f.mtime
            puts "Reloading #{f.path}" # was modified
            load f.path
            count += 1
          end
          @files[f] = f.mtime # update modified time
        end
        count
      end
      
    end
  end
end