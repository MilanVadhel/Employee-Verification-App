package com.example.employefy;

public interface GETURL
{
    public String url="http://192.168.0.103/EmployeFy/";
    public String signup_employee=url+"signup.php";
    public String login_employee=url+"login.php";
    public String verify_profile=url+"verify.php";
    public String request_url=url+"request.php";
    public String pending_employee_list=url+"pendingProfiles.php";
    public String preview_card=url+"previewcard.php";
    public String updateToken_url=url+"updatetoken.php";
    public String updatetoken_manager=url+"updatetokenofmanager.php";
    public String notification_url=url+"sendnotificationtomanager.php";
}
