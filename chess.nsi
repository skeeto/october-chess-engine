!include "MUI2.nsh"

Name "October Chess Engine"
Outfile "dist\october-chess-${VERSION}-installer.exe"
XPStyle on

!insertmacro MUI_PAGE_WELCOME
!insertmacro MUI_PAGE_DIRECTORY
!insertmacro MUI_PAGE_COMPONENTS
!insertmacro MUI_PAGE_INSTFILES
!insertmacro MUI_PAGE_FINISH

!insertmacro MUI_UNPAGE_CONFIRM
!insertmacro MUI_UNPAGE_INSTFILES

InstallDir "$PROGRAMFILES\October Chess"

!insertmacro MUI_LANGUAGE "English"

!define ADDREM "Software\Microsoft\Windows\CurrentVersion\Uninstall"

section
  SetShellVarContext all
  setOutPath $INSTDIR
  file dist\Chess.exe
  file dist\Chess.jar
  writeUninstaller $INSTDIR\uninstall.exe
  WriteRegStr HKLM "${ADDREM}\OctoberChess" \
                   "DisplayName" "October Chess"
  WriteRegStr HKLM "${ADDREM}\OctoberChess" \
  	           "UninstallString" "$\"$INSTDIR\uninstall.exe$\""
  WriteRegStr HKLM "${ADDREM}\OctoberChess" \
                   "QuietUninstallString" "$\"$INSTDIR\uninstall.exe$\" /S"
sectionEnd

section "Start Menu shortcut" StartMenuLnk
  createShortCut "$SMPROGRAMS\October Chess.lnk" "$INSTDIR\Chess.exe"
sectionEnd

section "Desktop shortcut" DesktopLnk
  createShortCut "$DESKTOP\October Chess.lnk" "$INSTDIR\Chess.exe"
sectionEnd

section "Source code" SourceCode
  file dist\Chess-src*.zip
  file dist\javadoc.zip
sectionEnd

section "Uninstall"
  SetShellVarContext all
  delete $INSTDIR\uninstall.exe
  RMDir /r $INSTDIR
  delete "$SMPROGRAMS\October Chess.lnk"
  delete "$DESKTOP\October Chess.lnk"
  DeleteRegKey HKLM "${ADDREM}\OctoberChess"
sectionEnd


; Section descriptions

LangString DESC_SourceCode ${LANG_ENGLISH} \
           "The Java source code of the Chess engine."
LangString DESC_DesktopLnk ${LANG_ENGLISH} \
           "Create a shortcut on your desktop."
LangString DESC_StartMenuLnk ${LANG_ENGLISH} \
           "Add a root Start Menu shortcut."

!insertmacro MUI_FUNCTION_DESCRIPTION_BEGIN
  !insertmacro MUI_DESCRIPTION_TEXT ${SourceCode} $(DESC_SourceCode)
  !insertmacro MUI_DESCRIPTION_TEXT ${DesktopLnk} $(DESC_DesktopLnk)
  !insertmacro MUI_DESCRIPTION_TEXT ${StartMenuLnk} $(DESC_StartMenuLnk)
!insertmacro MUI_FUNCTION_DESCRIPTION_END
