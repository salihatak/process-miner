
# Process Mining App

This application allows you to analyze and visualize business process flows from event logs. Upload your process log file, and the app will extract variants, activities, and process paths to help you discover insights and bottlenecks in your processes.

## Running the App Locally

1. **Clone the repository** (if you haven't already):
   ```sh
   git clone <your-repo-url>
   cd processminig
   ```

2. **Build the project** (requires Java 17+ and Gradle):
   ```sh
   ./gradlew build
   ```

3. **Run the application:**
   ```sh
   ./gradlew bootRun
   ```

4. **Access the app:**
   - The application will start on [http://localhost:8080](http://localhost:8080) by default.

5. **Upload your log file:**
   - Use the UI or API to upload/import your `process_log.csv` file.

## Usage

- Make sure your log file matches the format above.
- The timestamp must be in UTC and follow the required format.
- Upload or import the log file into the application to start process mining.

## Example Log File Format

Below is an example log file you can use to test the application. The date format must be `yyyy-MM-dd'T'HH:mm:ss'Z'` (e.g., `2024-06-01T09:00:00Z`).

Save this as `process_log.csv`:

```csv
case_id,activity,timestamp
1,Start,2024-06-01T09:00:00Z
1,Submit Application,2024-06-01T09:05:00Z
1,Review Application,2024-06-01T09:10:00Z
1,Approve Application,2024-06-01T09:20:00Z
1,End,2024-06-01T09:30:00Z
2,Start,2024-06-01T10:00:00Z
2,Submit Application,2024-06-01T10:05:00Z
2,Review Application,2024-06-01T10:15:00Z
2,Reject Application,2024-06-01T10:25:00Z
2,End,2024-06-01T10:30:00Z
3,Start,2024-06-01T11:00:00Z
3,Submit Application,2024-06-01T11:05:00Z
3,Review Application,2024-06-01T11:10:00Z
3,Approve Application,2024-06-01T11:20:00Z
3,End,2024-06-01T11:30:00Z
```