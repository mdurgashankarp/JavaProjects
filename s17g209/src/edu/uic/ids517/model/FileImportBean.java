package edu.uic.ids517.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FilenameUtils;
import org.apache.myfaces.custom.fileupload.UploadedFile;

@ManagedBean(name = "actionBeanFile")
@SessionScoped
public class FileImportBean {

	private MessagesBean messagesBean;
	private UploadedFile uploadedFile;
	private String fileLabel;
	private String fileName;
	private long fileSize;
	private String fileContentType;
	private int numberRows;
	private int numberColumns;
	private String uploadedFileContents;
	private boolean fileImport;
	private boolean fileImportError;
	private String filePath;
	private String tempFileName;
	private String tempFile;
	private String header;
	private String fileType;
	private String fileFormat;
	private FacesContext facesContext;
	private DbAccessBean dbAccessBean;
	private Connection connection;
	private PreparedStatement preparedStatement;
	private StringBuilder out;
	private boolean error;

	@PostConstruct
	public void init() {
		Map<String, Object> m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		dbAccessBean = (DbAccessBean) m.get("dbAccessBean");
		messagesBean = (MessagesBean) m.get("messagesBean");
		if (messagesBean == null) {
			messagesBean = new MessagesBean();
		}
	}

	/* To Process File */
	public String processFileUpload() {
		out = new StringBuilder();
		messagesBean.resetAll();
		error = false;
		uploadedFileContents = null;
		facesContext = FacesContext.getCurrentInstance();
		filePath = facesContext.getExternalContext().getRealPath("/temp");
		File tempFile = null;
		FileOutputStream fos = null;
		int n = 0;
		fileImport = false;
		try {
			if (uploadedFile == null) {
				return "FAIL";
			}
			String uploadedFileName = FilenameUtils.getBaseName(uploadedFile.getName());
			String uploadedFileExtension = FilenameUtils.getExtension(uploadedFile.getName());
			if (!(uploadedFileExtension.equalsIgnoreCase(fileFormat)
					|| (uploadedFileExtension.equalsIgnoreCase("txt") && fileFormat.equalsIgnoreCase("text"))
					|| (uploadedFileExtension.equalsIgnoreCase("xls") && fileFormat.equalsIgnoreCase("text")))) {
				messagesBean.setErrorMessage(
						"Mismatch in file uploaded and selected format. Upload " + uploadedFileName + " again!! \n");
				messagesBean.setRenderErrorMessage(true);
				fileImportError = true;
				fileImport = false;
				return "FAIL";
			}
			fileName = uploadedFile.getName();
			String baseName = FilenameUtils.getBaseName(fileName);
			fileSize = uploadedFile.getSize();
			fileContentType = uploadedFile.getContentType();
			// next line if want upload in String for memory processing
			uploadedFileContents = new String(uploadedFile.getBytes());
			tempFileName = filePath + "/" + baseName;
			tempFile = new File(tempFileName);
			fos = new FileOutputStream(tempFile);
			// next line if want file uploaded to disk
			fos.write(uploadedFile.getBytes());
			fos.close();
			Scanner s = new Scanner(tempFile);
			String input = "";
			String[] stInput = new String[100];
			ArrayList<String[]> as = new ArrayList<String[]>();
			int numberOfColumns = 0;
			Boolean t = true;
			ResultSet resultSet = null;
			ResultSetMetaData resultSetmd = null;

			if (fileType.equals("d")) {
				String sqlQuery = "select * from s17g209_" + fileLabel;
				String tableExists = dbAccessBean.execute(sqlQuery);
				if (tableExists.equalsIgnoreCase("SUCCESS")) {
					resultSet = dbAccessBean.getResultSet();

					if (resultSet != null) {
						resultSetmd = (ResultSetMetaData) resultSet.getMetaData();
					}
				}
			}
			int nr = 0;
			while (s.hasNext()) {
				input = s.nextLine();
				if (header.equals("y") && nr == 0) {
					nr++;
					continue;
				}
				if (fileFormat.equals("csv")) {
					stInput = input.split(",");
				} else if (fileFormat.equals("tab") || fileFormat.equals("text")) {
					stInput = input.split("\t");
				}
				if (fileType.equals("d")) {
					boolean skipLine = false;
					for (int x = 0; x < stInput.length; x++) {
						String datatype = resultSetmd.getColumnTypeName(x + 1);

						if (!datatype.equalsIgnoreCase("varchar")) {
							try {
								Double.parseDouble(stInput[x]);
							} catch (Exception e) {
								skipLine = true;
								error = true;

								out.append(input + "\n");
								continue;
							}
						}

					}
					if (!skipLine) {
						as.add(stInput);
					}
				} else {
					as.add(stInput);
				}
				// do something using input line at a time
				n++;
			}

			if (fileType.equals("m")) {
				createTable(fileLabel, as);
			} else if (fileType.equals("d")) {
				t = insertIntoTable(fileLabel, as);
			}
			{
				numberRows = as.size();
				fileImport = true;
				s.close();
			}
			if (t) {
				dbAccessBean.transactionLog("CREATE_TRANSACTION_IMPORT");
				return "SUCCESS";
			} else
				fileImport = false;
			return "FAIL";
		} catch (IOException e) {
			e.printStackTrace();
			e.getMessage();
			messagesBean.setErrorMessage("There is some error in uploading data");
			messagesBean.setRenderErrorMessage(true);
			return "FAIL";
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
			messagesBean.setErrorMessage("There is some error in uploading data");
			messagesBean.setRenderErrorMessage(true);
			return "FAIL";
		}

	}

	/* To Create Table */
	public boolean createTable(String fileLabel01, ArrayList<String[]> test) {
		StringBuilder sb = new StringBuilder();

		if (header.equals("n")) {

			for (int i = 0; i < test.size(); i++) {
				String[] stInput1 = test.get(i);
				if (stInput1[1].equalsIgnoreCase("string")) {
					stInput1[1] = "varchar(255)";
				}
				sb.append(stInput1[0]).append(" ").append(stInput1[1]);
				if (i < (test.size() - 1)) {
					sb.append(",");
				}
			}
		} else if (header.equals("y")) {

			for (int i = 0; i < test.size(); i++) {
				String[] stInput1 = test.get(i);
				if (stInput1[1].equalsIgnoreCase("string")) {
					stInput1[1] = "varchar(255)";
				}
				sb.append(stInput1[0]).append(" ").append(stInput1[1]);
				if (i < (test.size() - 1)) {
					sb.append(",");
				}

			}

		}
		numberColumns = test.get(0).length;

		String createTbl = "CREATE TABLE " + "s17g209_" + fileLabel01 + "(" + sb.toString() + ")";
		try {

			dbAccessBean.processQuery(createTbl);
			messagesBean.setRenderErrorMessage(true);
			messagesBean.setErrorMessage("Table created successfully");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;

	}

	/* To Insert into Table */
	public boolean insertIntoTable(String tableLabel, ArrayList<String[]> columnValues) {
		try {
			// dbAccessBean.getConnection().setAutoCommit(false);
			String sqlQuery = "select * from s17g209_" + tableLabel;
			// StringBuilder out = new StringBuilder();
			// boolean error = false;
			String tableExists = dbAccessBean.execute(sqlQuery);
			if (tableExists.equalsIgnoreCase("SUCCESS")) {
				ResultSet resultSet = dbAccessBean.getResultSet();
				ResultSetMetaData resultSetmd = null;
				if (resultSet != null) {
					resultSetmd = (ResultSetMetaData) resultSet.getMetaData();
				}
				StringBuilder stringBuilder = new StringBuilder();
				if (header.equals("n")) {
					for (int i = 0; i < columnValues.size(); i++) {

						stringBuilder.append("(");
						String[] sqlInput = columnValues.get(i);
						// Step 2
						int count = 0;
						for (String j : sqlInput) {
							if (j != null) {
								count++;
							}
						}

						// Step 3
						String[] newArray = new String[count];

						// Step 4
						int index = 0;
						for (String j : sqlInput) {
							if (j != null) {
								newArray[index++] = j;
							}
						}
						for (int k = 0; k < newArray.length; k++) {
							String datatype = resultSetmd.getColumnTypeName(k + 1);

							if (datatype.equalsIgnoreCase("varchar")) {
								stringBuilder.append("'");
								stringBuilder.append(newArray[k]);
								stringBuilder.append("'");
							} else {
								try {
									stringBuilder.append(newArray[k]);
									Double.parseDouble(newArray[k]);
								} catch (Exception e) {
									error = true;

									// out.append(Arrays.toString(columnValues.get(i))
									// + "\n");
									continue;
								}
							}

							if (k < newArray.length - 1) {
								stringBuilder.append(",");
							}
						}
						if (dbAccessBean.getNumberOfCols() != newArray.length) {
							for (int a = 0; a < dbAccessBean.getNumberOfCols() - newArray.length; a++) {
								stringBuilder.append(",");
								stringBuilder.append("null");
							}
						}
						stringBuilder.append(")");
						if (i < (columnValues.size() - 1)) {
							stringBuilder.append(",");
						}
					}

				} else if (header.equals("y")) {
					for (int i = 0; i < columnValues.size(); i++) {
						stringBuilder.append("(");
						String[] sqlInput = columnValues.get(i);
						// Step 2
						int count = 0;
						for (String j : sqlInput) {
							if (j != null) {
								count++;
							}
						}

						// Step 3
						String[] newArray = new String[count];

						// Step 4
						int index = 0;
						for (String j : sqlInput) {
							if (j != null) {
								newArray[index++] = j;
							}
						}
						for (int k = 0; k < newArray.length; k++) {
							String datatype = resultSetmd.getColumnTypeName(k + 1);
							if (datatype.equalsIgnoreCase("varchar")) {
								stringBuilder.append("'");
								stringBuilder.append(newArray[k]);
								stringBuilder.append("'");
							} else {
								try {
									stringBuilder.append(newArray[k]);
									Double.parseDouble(newArray[k]);
								} catch (Exception e) {
									error = true;

									// out.append(Arrays.toString(columnValues.get(i))
									// + "\n");
									continue;
								}
							}
							if (k < newArray.length - 1) {
								stringBuilder.append(",");
							}
						}
						if (dbAccessBean.getNumberOfCols() != newArray.length) {
							for (int a = 0; a < dbAccessBean.getNumberOfCols() - newArray.length; a++) {
								stringBuilder.append(",");
								stringBuilder.append("null");
							}
						}
						stringBuilder.append(")");
						if (i < (columnValues.size() - 1)) {
							stringBuilder.append(",");
						}

					}
				}
				numberColumns = columnValues.get(0).length;
				if (error) {
					messagesBean.setErrorMessage(
							"You have uploaded file which contains errors. These errors in data has been removed and uploaded!!" + "\n"
									+ "\n" + out.toString());
					messagesBean.setRenderErrorMessage(true);

					this.fileImportError = true;
					// dbAccessBean.getConnection().rollback();
					// dbAccessBean.getConnection().setAutoCommit(true);
					// return false;
				}
				String insertQuery = "INSERT INTO " + "s17g209_" + tableLabel + " VALUES " + stringBuilder.toString();

				try {

					dbAccessBean.execute(insertQuery);
					if(!error){
					messagesBean.setRenderErrorMessage(true);
					messagesBean.setErrorMessage("Inserted into table successfully");
					}
					//dbAccessBean.getConnection().commit();
					dbAccessBean.getConnection().setAutoCommit(true);
					

				} catch (Exception e) {
					messagesBean.setErrorMessage(
							"You have uploaded file which contains errors. These errors in data has been removed and uploaded!!" + " \r\n"
									+ out.toString());
					this.fileImportError = true;

					e.printStackTrace();
				}
				return true;
			} else {
				fileImport = false;
				messagesBean.setErrorMessage("The MetaData table does not EXIST. Create MetaData Table first");
				messagesBean.setRenderErrorMessage(true);
				return false;
			}
		} catch (Exception e) {
			messagesBean.setErrorMessage("You have uploaded file which contains errors. These errors in data has been removed and uploaded!!"
					+ " \r\n" + out.toString());
			messagesBean.setRenderErrorMessage(true);
			this.fileImportError = true;
			// e.printStackTrace();
			return false;
		}

	}

	public MessagesBean getMessageBean() {
		return messagesBean;
	}

	public void setMessageBean(MessagesBean messageBean) {
		this.messagesBean = messageBean;
	}

	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public String getFileLabel() {
		return fileLabel;
	}

	public void setFileLabel(String fileLabel) {
		this.fileLabel = fileLabel;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileContentType() {
		return fileContentType;
	}

	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}

	public int getNumberRows() {
		return numberRows;
	}

	public void setNumberRows(int numberRows) {
		this.numberRows = numberRows;
	}

	public int getNumberColumns() {
		return numberColumns;
	}

	public void setNumberColumns(int numberColumns) {
		this.numberColumns = numberColumns;
	}

	public String getUploadedFileContents() {
		return uploadedFileContents;
	}

	public void setUploadedFileContents(String uploadedFileContents) {
		this.uploadedFileContents = uploadedFileContents;
	}

	public boolean isFileImport() {
		return fileImport;
	}

	public void setFileImport(boolean fileImport) {
		this.fileImport = fileImport;
	}

	public boolean isFileImportError() {
		return fileImportError;
	}

	public MessagesBean getMessagesBean() {
		return messagesBean;
	}

	public void setMessagesBean(MessagesBean messageBean) {
		this.messagesBean = messageBean;
	}

	public void setFileImportError(boolean fileImportError) {
		this.fileImportError = fileImportError;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getTempFileName() {
		return tempFileName;
	}

	public void setTempFileName(String tempFileName) {
		this.tempFileName = tempFileName;
	}

	public FacesContext getFacesContext() {
		return facesContext;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFileFormat() {
		return fileFormat;
	}

	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}

	public void setFacesContext(FacesContext facesContext) {
		this.facesContext = facesContext;
	}

	public String resetMessage() {
		fileImport = false;
		messagesBean.setErrorMessage("");
		messagesBean.setRenderErrorMessage(false);
		return "HOME";

	}

}