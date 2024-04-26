awk '{printf "%s\\n", $0}' ../src/main/resources/application.yml | sed 's/"/\\"/g' > application_converted.yml
