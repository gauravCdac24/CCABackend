# SMS DLT Template Trigger Walkthrough (CCALMS)

Config location: `notification-service/src/main/resources/application.properties`
Resolver: `SmsTemplateResolver` — callers pass `smsTemplateKey` + `smsVars` only.

| Key | Template ID | Frontend trigger (button / page) | Backend trigger | Wired now? |
|---|---|---|---|---|
| `otp` | 1477178841413978234 | Login OTP modal (`Login.js` / `OTP.js`); Forgot Password OTP (`ForgotPassword.js`) | `OTPService.generateOtp` / `generateForgotOTP` | Yes |
| `intent-registration` | 1407174244996769199 | Public **Submit** on Intent Registration form | `UserLoginService.saveNewUserLogin` after intent creates user | Yes |
| `license-application` | 1407174245138297647 | Applicant **Submit Application** (Declaration pages) | Status → Submitted in `IndividualApplicationFormController.addDeclarization` | Yes |
| `application-review` | 1407174245390975022 | Review Committee **Review the Application** (return for edit) | ApplicationReview → EDIT_UPON_REVIEW | No |
| `application-acceptance` | 1407174245401388674 | Review Committee **Approval** | ApplicationReview → APPROVED_FROM_REVIEW | No |
| `recommendation-for-audit` | 1407174245419815641 | CCA **Approval** on Approve Application Form | Status → RECOMANDED_FOR_AUDIT | No |
| `audit-agency-selection` | 1407174245434511953 | CCA observations / reject on Audit Agency Selection | Audit approve/reject undertaking APIs | No |
| `audit-report-review` | 1407174245455070954 | Auditor **Forward to Applicant** / NC remarks | `ApplicationAuditController` NC forward / remarks | Yes |
| `application-approval` | 1407174245461980784 | CCA **Grant In-Principle Approval** | `LicenseIssuanceController` grant-in-principle | Yes |
| `license-issuance` | 1407174245469975657 | CCA **Issue License** (new) | `LicenseIssuanceController` issue license (`issued`) | Yes |
| `esp-application-review` | 1407174245493482131 | ESP review return/issues | esign-application-service review | No |
| `esp-go-live` | 1407174245505853129 | **Approve eSign Go Live** (after Recommend for Go Live) | `approve-esign-go-live` API | Yes |
| `application-rejection` | 1407174245514739243 | CCA / Committee reject application | NC rejection path in audit-service (partial) | Partial |
| `show-cause` | 1407174245522683893 | *(no UI yet)* | *(no backend yet)* | No |
| `license-suspension` | 1407174245530162035 | *(no UI yet)* | *(no backend yet)* | No |
| `cessation-application` | 1407174245552980419 | Licensee cessation **Submit** | `LicenseCessationApplicationController.handleSubmit` / `handleEditData` | Yes |
| `cessation-approval` | 1407174245569331287 | CCA **Approve Cessation** | `LicenseCessationApplicationController.approveApplicationForCCA` | Yes |
| `renewal-application` | 1407174245579282317 | Licensee renewal **Submit Application** | renewal-service `addDeclarization` → `SUBMITTED-FOR-RENEWAL` | Yes |
| `renewal-approval` | 1407174245588855486 | Grant in-principle on renewal track | Same grant API; uses this key when applicant already has a license | Yes |
| `license-renewal` | 1407174245600648226 | CCA **Issue License** when prior license existed | issue license (`renewed`) | Yes |

## Caller pattern

```java
notification.setNotificationType("Email & SMS"); // or All / SMS & Dashboard
notification.setSmsTemplateKey(SmsTemplateKey.LICENSE_APPLICATION);
notification.setSmsVars(List.of(applicantName, applicationNo));
notificationServ.sendNotification(notification);
```

Do **not** put free-form SMS text for DLT; body comes from config.
