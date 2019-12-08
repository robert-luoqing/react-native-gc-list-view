require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "react-native-gc-list-view"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.description  = "GC List View"
  s.homepage     = "https://github.com/robert-luoqing/react-native-gc-list-view"
  s.license      = "MIT"
  # s.license    = { :type => "MIT", :file => "FILE_LICENSE" }
  s.authors      = { "Robert" => "robert_luoqing@hotmail.com" }
  s.platforms    = { :ios => "9.0" }
  s.source       = { :git => "https://github.com/robert-luoqing/react-native-gc-list-view.git", :tag => "#{s.version}" }

  s.source_files = "ios/**/*.{h,m,swift}"
  s.requires_arc = true

  s.dependency "React"
  # ...
  # s.dependency "..."
end

