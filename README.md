csv2json
========

Provides a simple command line tool to convert a csv file into a json file, whereby each csv line is converted into a JSON array.

Command line options:

 * -f : input csv filename
 * -d : csv field delimiter (optional, default is ",")
 * -i : comma separated lines numbers to ignore (line numbers start with 1), for instance : 1,2,3
 * -o : JSON output file name
 * -a : output JSON array variable name (optional)

Build : mvn clean install

Example 1 : java -jar target/csv2json.jar -f data.csv -i 1 -a data

Input file:

	"title1","title1","title3","title4","title5"
    "data1","data2","data3",,
    "data1","data2","data3","data4","data5"

Produces: output file data.json

    data=[
    ["data1","data2","data3","",""],
    ["data1","data2","data3","data4","data5"]
    ]
   
Example 2 : java -jar target/csv2json.jar -f data2.csv -i 1 -d #

Input file: (numbers and "#" csv field delimeter used here)

	"title1"#"title2"#"title3"#"title4"#"title5"
    "data1"#"data2"#"data3"##
    1#2#3#4#5

Produces: output file data2.json

    [
    ["data1","data2","data3","",""],
    ["1","2","3","4","5"]
    ]   
