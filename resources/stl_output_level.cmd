@ECHO OFF
echo ====================================================================
echo Please select one of the following options for the server log level:
echo 1) OFF     - No output.
echo 2) FATAL   - Only fatal, program ending errors.
echo 3) ERROR   - Errors during executions (not fatal), as well as fatal errors.
echo 4) WARN    - Warnings during execution, errors, and fatal errors.
echo 5) INFO    - Standard output, as well as warnings, errors, and fatal errors.
echo 6) DEBUG   - Everything that needs to be, will be printed, including standard output, warnings, errors and fatal errors.
set /p Selection="Please enter the number of selected option:"
goto check_value

:check_value
if %Selection% GTR 6 goto prompt_again
if %Selection% LSS 1 goto prompt_again
goto submit

:prompt_again
echo The value you provided isn't valid, please enter a value in the range of 1-6.
set /p Selection="Please enter the number of selected option:"
goto check_value

:submit
set /a Selection-=1
echo %Selection% > .gubed
exit