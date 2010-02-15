package deltix.util.io;

import java.io.*;
import java.util.*;
import java.util.logging.*;

import javax.servlet.*;
import javax.servlet.http.*;

import deltix.util.lang.*;

public class FileUploadBean {

    private String                     _savePath, _filepath, _filename, _contentType;
    private Dictionary<String, String> _fields;

    public String getFilename () {
        return _filename;
    }

    public String getFilepath () {
        return _filepath;
    }

    public void setSavePath (final String savePath) {
        this._savePath = savePath;
    }

    public String getContentType () {
        return _contentType;
    }

    public String getFieldValue (final String fieldName) {
        if (_fields == null || fieldName == null)
            return null;
        return _fields.get (fieldName);
    }

    private void setFilename (final String s) {
        if (s == null)
            return;

        int pos = s.indexOf ("filename=\"");
        if (pos != -1) {
            _filepath = s.substring (pos + 10,
                                     s.length () - 1);
            // Windows browsers include the full path on the client
            // But Linux/Unix and Mac browsers only send the filename
            // test if this is from a Windows browser
            pos = _filepath.lastIndexOf ("\\");
            if (pos != -1)
                _filename = _filepath.substring (pos + 1);
            else
                _filename = _filepath;
        }
    }

    private void setContentType (final String s) {
        if (s == null)
            return;

        final int pos = s.indexOf (": ");
        if (pos != -1)
            _contentType = s.substring (pos + 2,
                                        s.length ());
    }

    public void doUpload (final HttpServletRequest request) throws IOException {
        final ServletInputStream in = request.getInputStream ();

        final byte[] line = new byte[128];
        int i = in.readLine (line,
                             0,
                             128);
        if (i < 3)
            return;
        final int boundaryLength = i - 2;

        final String boundary = new String (line,
                                            0,
                                            boundaryLength); // -2 discards the
                                                             // newline
                                                             // character
        _fields = new Hashtable<String, String> ();

        while (i != -1) {
            String newLine = new String (line,
                                         0,
                                         i);
            if (newLine.startsWith ("Content-Disposition: form-data; name=\"")) {
                if (newLine.indexOf ("filename=\"") != -1) {
                    setFilename (new String (line,
                                             0,
                                             i - 2));
                    if (_filename == null)
                        return;
                    // this is the file content
                    i = in.readLine (line,
                                     0,
                                     128);
                    setContentType (new String (line,
                                                0,
                                                i - 2));
                    i = in.readLine (line,
                                     0,
                                     128);
                    // blank line
                    i = in.readLine (line,
                                     0,
                                     128);
                    newLine = new String (line,
                                          0,
                                          i);
                    final PrintWriter pw = new PrintWriter (new BufferedWriter (new
                                                                                FileWriter ((_savePath == null
                                                                                                              ? ""
                                                                                                              : _savePath) +
                                                                                            _filename)));
                    while (i != -1 && !newLine.startsWith (boundary)) {
                        // the problem is the last line of the file content
                        // contains the new line character.
                        // So, we need to check if the current line is
                        // the last line.
                        i = in.readLine (line,
                                         0,
                                         128);
                        // +4 is eof
                        if ((i == boundaryLength + 2 || i == boundaryLength + 4) &&
                            (new String (line,
                                         0,
                                         i).startsWith (boundary)))
                            pw.print (newLine.substring (0,
                                                         newLine.length () - 2));
                        else
                            pw.print (newLine);
                        newLine = new String (line,
                                              0,
                                              i);

                    }
                    pw.close ();

                } else {
                    // this is a field
                    // get the field name
                    final int pos = newLine.indexOf ("name=\"");
                    final String fieldName = newLine.substring (pos + 6,
                                                                newLine.length () - 3);
                    // System.out.println("fieldName:" + fieldName);
                    // blank line
                    i = in.readLine (line,
                                     0,
                                     128);
                    i = in.readLine (line,
                                     0,
                                     128);
                    newLine = new String (line,
                                          0,
                                          i);
                    final StringBuffer fieldValue = new StringBuffer (128);
                    while (i != -1 && !newLine.startsWith (boundary)) {
                        // The last line of the field
                        // contains the new line character.
                        // So, we need to check if the current line is
                        // the last line.
                        i = in.readLine (line,
                                         0,
                                         128);
                        // +4 is eof
                        if ((i == boundaryLength + 2 || i == boundaryLength + 4)
                            &&
                            (new String (line,
                                         0,
                                         i).startsWith (boundary)))
                            fieldValue.append (newLine.substring (0,
                                                                  newLine.length () - 2));
                        else
                            fieldValue.append (newLine);
                        newLine = new String (line,
                                              0,
                                              i);
                    }
                    Util.LOGGER.log (Level.INFO, fieldName + "<->"+fieldValue.toString ());
                    _fields.put (fieldName,
                                 fieldValue.toString ());
                }
            }
            i = in.readLine (line,
                             0,
                             128);

        } // end while
    }
}
